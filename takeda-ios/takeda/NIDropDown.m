//
//  NIDropDown.m
//  NIDropDown
//
//  Created by Bijesh N on 12/28/12.
//  Copyright (c) 2012 Nitor Infotech. All rights reserved.
//

#import "NIDropDown.h"
#import "QuartzCore/QuartzCore.h"


#pragma mark  Cell

@interface D1TableCell : UITableViewCell

@property (nonatomic, strong) UILabel *name;
@property (nonatomic, strong) UILabel *descr1;
@property (nonatomic, strong) UILabel *descr2;
@property (nonatomic, strong) UIImageView *drugLogo;
@property (nonatomic, strong) UIButton *button;

@end


@implementation D1TableCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setupInterface];
    }
    return self;
}

-(void)setupInterface{
  
    UIView * v = [[UIView alloc] init];
    v.backgroundColor = [UIColor grayColor];
    self.selectedBackgroundView = v;

    
    self.backgroundColor = RGB(250, 250, 250);
    
    self.drugLogo = [[UIImageView alloc] initWithFrame:CGRectMake(15, 10, 30, 30)];
    self.name = [[UILabel alloc] initWithFrame:CGRectMake(50, 4, self.frame.size.width - 50, 20)];
    self.descr1 = [[UILabel alloc] initWithFrame:CGRectMake(50, 20, self.frame.size.width - 50, 15)];
    self.descr2 = [[UILabel alloc] initWithFrame:CGRectMake(50, 30, self.frame.size.width - 50, 15)];
    self.button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    self.button.frame = self.frame;
    self.drugLogo.layer.borderColor = RGB(220, 240, 242).CGColor;

    self.drugLogo.backgroundColor = [UIColor clearColor];
    self.name.backgroundColor = [UIColor clearColor];
    self.descr1.backgroundColor = [UIColor clearColor];
    self.descr2.backgroundColor = [UIColor clearColor];

    self.name.font = [UIFont fontWithName:@"Roboto-Regular" size:12];
    self.name.textColor = RGB(98, 110, 111);
    
    [self.button setImage:[self imageWithColor:RGBA(0, 0, 0, 0.3)] forState:UIControlStateHighlighted];
    
    self.descr1.font = [UIFont fontWithName:@"Roboto-Regular" size:9];
    self.descr1.textColor = RGB(129, 145, 156);
   
    self.descr2.font = [UIFont fontWithName:@"Roboto-Regular" size:9];
    self.descr2.textColor = RGB(129, 145, 156);
    
    [self addSubview:self.drugLogo];
    [self addSubview:self.name];
    [self addSubview:self.descr1];
    [self addSubview:self.descr2];
    [self addSubview:self.button];
}

- (UIImage *)imageWithColor:(UIColor *)color
{
    CGRect rect = CGRectMake(0.0f, 0.0f, 1.0f, 1.0f);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return image;
}

@end


#pragma mark  Cell







@interface NIDropDown () <UITableViewDataSource,UITableViewDelegate>

@end

@implementation NIDropDown

@synthesize btnSender;
@synthesize list;
@synthesize imageList;
@synthesize delegate;
@synthesize animationDirection;


- (id)showDropDown:(UIView*)b:(CGFloat *)height:(NSArray *)arr:(NSArray *)imgArr:(NSString *)direction {
    
    (void)[super init];
    
    btnSender = b;
    animationDirection = direction;

    CGFloat t;
    
    if (self.showClearBtn){
        t = 170;
    } else {
        t = 180;
    }
    
    height = &t;
    if (self.customWidth==0) self.customWidth = btnSender.width;
    if (self.customX==0) self.customX = btnSender.x;

    if (!self.delegateClass) self.delegateClass = (UIViewController*)self.delegate;
    
    if (self) {
        // Initialization code
        
        
        UIView *theSuperView;
        CGRect btn;
        
        if (IS_IPHONE && ![b.restorationIdentifier isEqualToString:@"nww"] && ![b.restorationIdentifier isEqualToString:@"nww1"]) {
            theSuperView = [self normSuperviewForView:b];
            btn = [self rectView:b inSuperView:theSuperView];
        } else {
            btn = b.frame;
        }
        
        self.list = [NSArray arrayWithArray:arr];
        self.imageList = [NSArray arrayWithArray:imgArr];
        if ([direction isEqualToString:@"up"]) {
            self.frame = CGRectMake(self.customX, btn.origin.y, self.customWidth, 0);
            self.layer.shadowOffset = CGSizeMake(-5, -5);
        }else if ([direction isEqualToString:@"down"]) {
            self.frame = CGRectMake(self.customX, btn.origin.y+btn.size.height, self.customWidth, 0);
            self.layer.shadowOffset = CGSizeMake(-5, 5);
        }
        
        self.layer.masksToBounds = NO;
        self.layer.cornerRadius = 8;
        self.layer.shadowRadius = 5;
        self.layer.shadowOpacity = 0.5;
        self.layer.borderColor = RGB(220, 240, 242).CGColor;
        self.layer.borderWidth = 1.0f;

        
        _table = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, self.customWidth, 0)];
        _table.layer.cornerRadius = 5;
        _table.backgroundColor = RGB(245, 245, 245);
        _table.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
        _table.separatorColor = [UIColor grayColor];
        _table.delegate = self;
        _table.dataSource = self;
        if (self.showClearBtn){
            _header = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.customWidth, 40)];
            _clearBtn = [[UIButton alloc] initWithFrame:_header.bounds];
            [_clearBtn setTitle:@"Очистить" forState:UIControlStateNormal];
            _clearBtn.titleLabel.font = self.fontNameText?self.fontNameText:((UIButton*)btnSender).titleLabel.font;
            [_clearBtn setTitleColor:self.colorText?self.colorText:[((UIButton*)btnSender) titleColorForState:UIControlStateNormal] forState:UIControlStateNormal];
            [_clearBtn addTarget:self action:@selector(clearAction:) forControlEvents:UIControlEventTouchUpInside];
            [_header addSubview:_clearBtn];
            _table.tableHeaderView = _header;
        }

        
        
        if (IOS7_AND_LATER) {
            _table.separatorInset = UIEdgeInsetsMake(0, 0, 0, 0);
        }
        
        [UIView beginAnimations:nil context:nil];
        [UIView setAnimationDuration:0.3];
        if ([direction isEqualToString:@"up"]) {
            self.frame = CGRectMake(self.customX, btn.origin.y-*height, self.customWidth, *height);
        } else if([direction isEqualToString:@"down"]) {
            self.frame = CGRectMake(self.customX, btn.origin.y+btn.size.height, self.customWidth, *height);
        }
        _table.frame = CGRectMake(0, 0, self.customWidth, *height);
        [UIView commitAnimations];
        if (IS_IPHONE && ![b.restorationIdentifier isEqualToString:@"nww"] && ![b.restorationIdentifier isEqualToString:@"nww1"]) {
            [theSuperView addSubview:self];
        } else {
            [b.superview addSubview:self];
        }
        self.clipsToBounds = YES;
        [self addSubview:_table];
        
    }
    
    _table.showsVerticalScrollIndicator = YES;
    _table.showsHorizontalScrollIndicator = NO;
    _table.tag = noDisableVerticalScrollTag;
    self.showed = YES;
    [self.table reloadData];
    [_table flashScrollIndicators];
    [_table scrollRectToVisible:CGRectMake(0, 0, 1, 1) animated:YES];
    return self;
}



-(void)setupResBlock:(void (^)(int))ResultBlock{
    self.resultBlock = ResultBlock;
}

-(UIView*)normSuperviewForView:(UIView*)theView{
    UIView *sup = theView.superview;
//    while (!(([sup isKindOfClass:[UIScrollView class]]&&[sup isKindOfClass:[UITableView class]]&&![sup.superview isKindOfClass:[UITableViewCell class]])||sup.superview == nil||(([self.delegateClass respondsToSelector:@selector(view)])&& sup == self.delegateClass.view))) {
//        sup = [self normSuperviewForView:sup];
//    }

    while (!(([sup isKindOfClass:[UIScrollView class]]&&![sup.superview isKindOfClass:[UITableViewCell class]])||sup.superview == nil||(([self.delegateClass respondsToSelector:@selector(view)])&& sup == self.delegateClass.view))) {
        sup = [self normSuperviewForView:sup];
    }
    return sup;
}

-(CGRect)rectView:(UIView*)theView inSuperView:(UIView*)theSuperview{
    CGRect theViewRect = theView.frame;
    UIView *currentSuperview = theView;//.superview;
    while (theSuperview!=currentSuperview) {
        currentSuperview = currentSuperview.superview;
        CGRect currentSuperviewRect = currentSuperview.frame;
        if (currentSuperview == theSuperview) {
            currentSuperviewRect.origin.y = 0;
        }
        theViewRect.origin.x += currentSuperviewRect.origin.x;
        theViewRect.origin.y += currentSuperviewRect.origin.y;
    }
    
    return theViewRect;
}



-(void)hideDropDown:(UIView*)b {
    self.showed = NO;
    UIView *theSuperView;
    CGRect btn;
    
    if (IS_IPHONE && ![b.restorationIdentifier isEqualToString:@"nww"] && ![b.restorationIdentifier isEqualToString:@"nww1"]) {
        theSuperView = [self normSuperviewForView:b];
        btn = [self rectView:b inSuperView:theSuperView];
    } else {
        btn = b.frame;
    }
    
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:0.3];
    if ([animationDirection isEqualToString:@"up"]) {
        self.frame = CGRectMake(self.customX, btn.origin.y, self.customWidth, 0);
    }else if ([animationDirection isEqualToString:@"down"]) {
        self.frame = CGRectMake(self.customX, btn.origin.y+btn.size.height, self.customWidth, 0);
    }
    _table.frame = CGRectMake(0, 0, self.customWidth, 0);
    [UIView commitAnimations];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 50;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.list count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.nLDropDownType == nD1){
        return [self tableView:tableView cellForRowAtIndexPathD1:indexPath];
    } else if (self.nLDropDownType == nD2){
        return [self tableView:tableView cellForRowAtIndexPathD2:indexPath];
    }
    else if (self.nLDropDownType == nD3){
        return [self tableView:tableView cellForRowAtIndexPathD3:indexPath];
    }else if(self.nLDropDownType == nD5)
    {
         return [self tableView:tableView cellForRowAtIndexPathD5:indexPath];
    }else if(self.nLDropDownType == normal6)
    {
        return [self tableView:tableView cellForRowAtIndexPathSimple:indexPath];
    }else {
        return [self tableView:tableView cellForRowAtIndexPathNorm:indexPath];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPathNorm:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell;// = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:(self.descriptionsList!=nil)?UITableViewCellStyleSubtitle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    if ([self.imageList count] == [self.list count]) {
        cell.textLabel.text =[list objectAtIndex:indexPath.row];
        cell.imageView.image = [imageList objectAtIndex:indexPath.row];
    } else if ([self.imageList count] > [self.list count]) {
        cell.textLabel.text =[list objectAtIndex:indexPath.row];
        if (indexPath.row < [imageList count]) {
            cell.imageView.image = [imageList objectAtIndex:indexPath.row];
        }
    } else if ([self.imageList count] < [self.list count]) {
        cell.textLabel.text =[list objectAtIndex:indexPath.row];
        if (indexPath.row < [imageList count]) {
            cell.imageView.image = [imageList objectAtIndex:indexPath.row];
        }
    }
    
    if (indexPath.row<self.descriptionsList.count){
        if (![self.descriptionsList[indexPath.row] isKindOfClass:[NSNull class]]){
            cell.detailTextLabel.text = self.descriptionsList[indexPath.row];
        } else {
            cell.detailTextLabel.text = @"";
        }
    } else {
        cell.detailTextLabel.text = @"";
    }
    
    UIView * v = [[UIView alloc] init];
    v.backgroundColor = RGB(244, 244, 244);
    cell.selectedBackgroundView = v;

    cell.textLabel.font = self.fontNameText?self.fontNameText:((UIButton*)btnSender).titleLabel.font;
    cell.textLabel.textAlignment = ((UIButton*)btnSender).titleLabel.textAlignment;
    cell.textLabel.textColor = self.colorText?self.colorText:[((UIButton*)btnSender) titleColorForState:UIControlStateNormal];
    
    cell.detailTextLabel.font = [UIFont fontWithName:cell.textLabel.font.fontName size:cell.textLabel.font.pointSize-2];
    cell.detailTextLabel.textColor = RGB(168, 182, 183);
    
    cell.backgroundColor = RGB(250, 250, 250);
    NSLog(@"index path = %i",indexPath.row);
    return cell;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPathD1:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    
    D1TableCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[D1TableCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
         }
    NSMutableDictionary *info = list[indexPath.row];
    cell.name.text = info[@"name"];
    cell.descr1.text = [info[@"lat_name"] Safed];
    cell.descr2.text = info[@"made_in"];
    cell.button.tag = indexPath.row;
    [cell.button addTarget:self action:@selector(buttonD1Click:) forControlEvents:UIControlEventTouchUpInside];

    if (info[@"image"]&&[info[@"image"] length]>0){
        //[cell.drugLogo setImageWithURL:info[@"image"] placeholderImage:[UIImage imageNamed:@"drugNoName"]];
        cell.drugLogo.layer.borderWidth = 1.0f;
    } else {
        [cell.drugLogo setImage:[UIImage imageNamed:@"drugNoName"]];
        cell.drugLogo.layer.borderWidth = 0.0f;
    }


    return cell;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPathSimple:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    cell.backgroundColor = RGB(250, 250, 250);
    cell.textLabel.text = self.list[indexPath.row];
    cell.textLabel.font = self.fontNameText?self.fontNameText:((UIButton*)btnSender).titleLabel.font;
    cell.textLabel.textAlignment = ((UIButton*)btnSender).titleLabel.textAlignment;
    cell.textLabel.textColor = self.colorText?self.colorText:[((UIButton*)btnSender) titleColorForState:UIControlStateNormal];
    
    
    return cell;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPathD5:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    
    D1TableCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[D1TableCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    NSMutableDictionary *info = list[indexPath.row];
    cell.name.text = info[@"name"];
    cell.descr1.text = [info[@"zipInfo"] Safed];
    cell.descr2.text = info[@"made_in"];
    cell.button.tag = indexPath.row;
    [cell.button addTarget:self action:@selector(buttonD1Click:) forControlEvents:UIControlEventTouchUpInside];
    
    if (info[@"image"]&&[info[@"image"] length]>0){
        //[cell.drugLogo setImageWithURL:info[@"image"] placeholderImage:[UIImage imageNamed:@"drugNoName"]];
        cell.drugLogo.layer.borderWidth = 1.0f;
    } else {
        [cell.drugLogo setImage:[UIImage imageNamed:@"drugNoName"]];
        cell.drugLogo.layer.borderWidth = 0.0f;
    }
    
    
    return cell;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPathD2:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:(self.descriptionsList!=nil)?UITableViewCellStyleSubtitle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    if ([self.imageList count] == [self.list count]) {
        cell.textLabel.text =[list objectAtIndex:indexPath.row];
        cell.imageView.image = [imageList objectAtIndex:indexPath.row];
    } else if ([self.imageList count] > [self.list count]) {
        cell.textLabel.text =[list objectAtIndex:indexPath.row];
        if (indexPath.row < [imageList count]) {
            cell.imageView.image = [imageList objectAtIndex:indexPath.row];
        }
    } else if ([self.imageList count] < [self.list count]) {
        cell.textLabel.text =[list objectAtIndex:indexPath.row];
        if (indexPath.row < [imageList count]) {
            cell.imageView.image = [imageList objectAtIndex:indexPath.row];
        }
    }
    
    if (indexPath.row<self.descriptionsList.count){
        if (![self.descriptionsList[indexPath.row] isKindOfClass:[NSNull class]]){
            cell.detailTextLabel.text = self.descriptionsList[indexPath.row];
        } else {
            cell.detailTextLabel.text = @"";
        }
    } else {
        cell.detailTextLabel.text = @"";
    }
    

    
    UIView * v = [[UIView alloc] init];
    v.backgroundColor = RGB(244, 244, 244);
    cell.selectedBackgroundView = v;
    
    cell.textLabel.font = ((UITextField*)btnSender).font;
    cell.textLabel.textAlignment = ((UITextField*)btnSender).textAlignment;
    cell.textLabel.textColor = [((UITextField*)btnSender) textColor];
    cell.backgroundColor = RGB(250, 250, 250);
    cell.detailTextLabel.font = [UIFont fontWithName:cell.textLabel.font.fontName size:cell.textLabel.font.pointSize-2];
    cell.detailTextLabel.textColor = RGB(168, 182, 183);

    
    NSLog(@"index path = %i",indexPath.row);
    return cell;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPathD3:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell;
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    cell.textLabel.text = self.list[indexPath.row][@"code"];
    cell.textLabel.font = ((UITextField*)btnSender).font;
   
   
    cell.backgroundColor = RGB(250, 250, 250);
    cell.detailTextLabel.font = [UIFont fontWithName:cell.textLabel.font.fontName size:cell.textLabel.font.pointSize-2];
    cell.detailTextLabel.textColor = RGB(168, 182, 183);
    cell.detailTextLabel.text = self.list[indexPath.row][@"name"];
    return cell;
}

-(IBAction)buttonClick:(id)sender{
    
    NSIndexPath *myIP = [NSIndexPath indexPathForRow:((UIButton *)sender).tag inSection:0];
    
    [self tableView:_table didSelectRowAtIndexPath:myIP];
    
}

-(IBAction)buttonD1Click:(UIButton*)sender{
    int index = sender.tag;
    [self hideDropDown:btnSender];
    self.resultBlock(index);
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSLog(@"selected = %i",indexPath.row);
    self.selectedIndex = indexPath.row;
    UITableViewCell *c = [tableView cellForRowAtIndexPath:indexPath];
    
    if (self.nLDropDownType == nD1){
        
    } else if (self.nLDropDownType == nD2){
        
    } else {
        [((UIButton*)btnSender) setTitle:c.textLabel.text forState:UIControlStateNormal];
        
        if (c.imageView.image) {
            for (UIView *subview in btnSender.subviews) {
                if ([subview isKindOfClass:[UIImageView class]]) {
                    [subview removeFromSuperview];
                }
            }
            imgView.image = c.imageView.image;
            imgView = [[UIImageView alloc] initWithImage:c.imageView.image];
            imgView.frame = CGRectMake(5, 5, 25, 25);
            [btnSender addSubview:imgView];
        }

    }
    
       [self hideDropDown:btnSender];
    if (self.resultBlock){
        self.resultBlock(self.selectedIndex);
    }
        [self myDelegate];
}

- (void) myDelegate {
    self.showed = NO;
    [self.delegate niDropDownDelegateMethod:self];
}

-(void)clearAction:(UIButton*)sender{
    self.showed = NO;
    if ([self.delegate respondsToSelector:@selector(niDropDownClearMethod:)]){
        [self.delegate performSelector:@selector(niDropDownClearMethod:) withObject:self];
        [self hideDropDown:self.sender];
        if ([self.delegate respondsToSelector:@selector(rel:)]){
            [self.delegate performSelector:@selector(rel:) withObject:self];
        }
    }
}


@end
