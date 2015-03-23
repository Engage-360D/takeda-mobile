//
//  AllSingle.h
//  ALL
//
//  Created by Serg on 12/20/13.
//  Copyright (c) 2013 soft-mobile. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

typedef void(^MyGlobalBlock)(void);

@interface AllSingle : NSObject{
    
    MyGlobalBlock hideKeyb_block_;
    MyGlobalBlock showKeyb_block_;
    
}

+(AllSingle*) sharedInstance;
+(void)resetData;

@property (nonatomic, assign) NSString *trax;
@property (nonatomic, strong) NSDictionary *DataTree;
@property (nonatomic, assign) id keybHolder;
@property (nonatomic, assign) id openedMenuController;

@property (nonatomic, strong) FMDatabase *database;



-(void)clearCashe;

@property (nonatomic, copy) MyGlobalBlock hideKeyb_block;
@property (nonatomic, copy) MyGlobalBlock showKeyb_block;


- (NSString *)pathToDB;
- (float)heightForImageConstWidth:(float)toWidth forSize:(CGSize)imgUrlSize;
- (float)widthForImageConstHeight:(float)toHeight forSize:(CGSize)imgUrlSize;
- (CGFloat)getHeight:(NSString*)text font:(UIFont*)font width:(float)width;
- (CGFloat)heightTextView:(UITextView*)textView;
- (CGFloat)heightLabel:(UILabel*)label;
- (CGSize)text:(NSString *)text sizeWithFont:(UIFont *)font constrainedToSize:(CGSize)size;
- (CGFloat)measureHeightOfUITextView:(UITextView *)textView;
- (CGFloat)heightForTextView:(UITextView*)textView containingString:(NSString*)string;
- (CGFloat)textViewHeightForAttributedText:(NSAttributedString *)text andWidth:(CGFloat)width;
- (NSArray*)parseUrslFromString:(NSString*)string;
- (NSString*)base64forData:(NSData*)theData;
- (NSData *)base64DataFromString: (NSString *)string;
- (UIImage *)imageWithImage:(UIImage *)image scaledToSize:(CGSize)newSize;
- (NSArray*)sortArray:(NSArray*)array byKey:(NSString*)key wayUp:(BOOL)up;
- (id) recursiveMutable:(id)object;
- (CGRect)rectView:(UIView*)theView inSuperView:(UIView*)theSuperview;
- (CLLocationCoordinate2D)locationFromString:(NSString*)string;
- (NSString*)fixUrl:(NSString*)url;
- (BOOL)isNotNull:(id)value;
- (NSString*)dictToStr:(NSDictionary*)dict;
- (int)servToTimest:(NSString*)strData;
- (NSDate*)servToDate:(NSString*)strData;
- (NSDate*)servToDateTimeSec:(NSString*)strData;
- (int)index:(id)tValue inArray:(NSArray*)array;
- (NSMutableDictionary*)values:(NSString*)values ForKeys:(NSString*)keys InArray:(NSArray*)array;
- (NSMutableDictionary*)dictionaryWithValue:(id)value ForKey:(NSString*)key InArray:(NSArray*)array;
- (NSDate*) parseDate:(NSString *)dateString;
- (NSDate*) parseTime:(NSString *)timeString;
- (NSDate*) parseDateTime:(NSString *)dateString;
- (NSString*) strDate:(NSDate *)date;
- (NSString*) strDateTime:(NSDate *)date;
- (void)callPhone:(NSString*)number;
- (UIColor*) randomColor;
- (void)removeSubviewsFrom:(UIView*)view;
- (NSString*)PathFromUrl:(NSString*)url;

@end
