//
//  AnalizDataUserPage.h
//  takeda
//
//  Created by Serg on 4/2/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AnalizDataUserPage : UIViewController
@property (nonatomic,retain) IBOutlet UIButton *nextStepBtn;
@property (nonatomic,retain) IBOutlet UILabel *titleRisk;
@property (nonatomic,retain) NSArray *sourceData;
@property (nonatomic,retain) IBOutlet UITableView *tableView;
-(void)reloadData;
@end
